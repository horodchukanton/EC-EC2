###########################################$
#  Copyright 2008-2010 Amazon.com, Inc. or its affiliates. All Rights Reserved.
#  Licensed under the Apache License, Version 2.0 (the "License"). You may not
#  use this file except in compliance with the License.
#  A copy of the License is located at
#
#      http://aws.amazon.com/apache2.0
#
#  or in the "license" file accompanying this file. This file is distributed on
#  an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
#  or implied. See the License for the specific language governing permissions
#  and limitations under the License.
###########################################$
#    __  _    _  ___
#   (  )( \/\/ )/ __)
#   /__\ \    / \__ \
#  (_)(_) \/\/  (___/
#
#  Amazon EC2 Perl Library
#  API Version: 2010-06-15
#  Generated: Wed Jul 21 13:37:54 PDT 2010
#


package Amazon::EC2::Model::Placement;

use base qw (Amazon::EC2::Model);



    #
    # Amazon::EC2::Model::Placement
    #
    # Properties:
    #
    #
    # AvailabilityZone: string
    # GroupName: string
    #
    #
    #
    sub new {
        my ($class, $data) = @_;
        my $self = {};
        $self->{_fields} = {

            AvailabilityZone => { FieldValue => undef, FieldType => "string"},
            GroupName => { FieldValue => undef, FieldType => "string"},
            Tenancy => {FieldValue => undef, FieldType => "string"},
        };

        bless ($self, $class);
        if (defined $data) {
           $self->_fromHashRef($data);
        }

        return $self;
    }


    sub getAvailabilityZone {
        return shift->{_fields}->{AvailabilityZone}->{FieldValue};
    }


    sub setAvailabilityZone {
        my ($self, $value) = @_;

        $self->{_fields}->{AvailabilityZone}->{FieldValue} = $value;
        return $self;
    }


    sub withAvailabilityZone {
        my ($self, $value) = @_;
        $self->setAvailabilityZone($value);
        return $self;
    }


    sub isSetAvailabilityZone {
        return defined (shift->{_fields}->{AvailabilityZone}->{FieldValue});
    }


    sub getTenancy {
        return shift->{_fields}->{Tenancy}->{FieldValue};
    }


    sub setTenancy {
        my ($self, $value) = @_;

        $self->{_fields}->{Tenancy}->{FieldValue} = $value;
        return $self;
    }


    sub withTenancy {
        my ($self, $value) = @_;
        $self->setTenancy($value);
        return $self;
    }

    sub isSetTenancy {
        return defined (shift->{_fields}->{Tenancy}->{FieldValue});
    }


    sub getGroupName {
        return shift->{_fields}->{GroupName}->{FieldValue};
    }


    sub setGroupName {
        my ($self, $value) = @_;

        $self->{_fields}->{GroupName}->{FieldValue} = $value;
        return $self;
    }


    sub withGroupName {
        my ($self, $value) = @_;
        $self->setGroupName($value);
        return $self;
    }


    sub isSetGroupName {
        return defined (shift->{_fields}->{GroupName}->{FieldValue});
    }





1;
